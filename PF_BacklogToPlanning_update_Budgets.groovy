// gets the value of import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.issue.search.SearchException
import com.atlassian.jira.web.bean.PagerFilter

def cfManager = ComponentAccessor.getCustomFieldManager()
def issueManager = ComponentAccessor.issueManager
def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def searchService = ComponentAccessor.getComponentOfType(SearchService)

//define custom field "Total course costs"
def cfCourseCosts = cfManager.getCustomFieldObject("customfield_13400")

//define customfield "budget"
def cfBudget = cfManager.getCustomFieldObject("customfield_13303")

//get issue to be updated (verplantes Budget Subtask)
MutableIssue planedBudgetIssue = issueManager.getIssueByCurrentKey("BES-14")

//get issue that triggered the workflow
MutableIssue triggerIssue = issue

// The JQL query you want to search with
final jqlSearch = "project = BES and issuetype=Course and status = 'In Planning'"


//this needs to be defined as a workaround as issue is not indexed fast enough to be detected by jql
//courseCosts of trigger issue need to be added manually after cumulated courseCosts of "Done" courses
def triggerCourseCosts = (Float) triggerIssue.getCustomFieldValue(cfCourseCosts)

// Parse the query
def parseResult = searchService.parseQuery(user, jqlSearch)
if (!parseResult.valid) {
    log.error('Invalid query')
    return null
}

def cumulativeCourseCosts = (Double) 0

try {
    // Perform the query to get the issues
    def results = searchService.search(user, parseResult.query, PagerFilter.unlimitedFilter)
    def issues = results.results
	log.warn("issues: "+issues)
    // log.info("result: " + issues)
    issues.each {
        log.info("current issue :" + it.key)
        def costs = (Float) it.getCustomFieldValue(cfCourseCosts)
        log.warn(costs)
        cumulativeCourseCosts = cumulativeCourseCosts + costs
        log.info("current cumulative: " + cumulativeCourseCosts)
                
    }
    
    //This adds the course costs from trigger issue from workaround above
    log.info("Course costs from trigger issue: " + triggerCourseCosts)
    cumulativeCourseCosts = cumulativeCourseCosts + triggerCourseCosts
	log.info("final calculation of course costs: " + cumulativeCourseCosts)
    
    //set value of budget field to newly calculated/current budget
	planedBudgetIssue.setCustomFieldValue(cfBudget, cumulativeCourseCosts)
    
    //update issue
	issueManager.updateIssue(user, planedBudgetIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
    //log.info(costArray)
    
	} catch (SearchException e) {
    	e.printStackTrace()
    	null
	}

//=====================================================================
//##### update "Verfügbares Budget" #############

///get issues:
def yearlyBudgetIssue = issueManager.getIssueByCurrentKey("BES-1") //JahresBudget-issue
def availableBudgetIssue = issueManager.getIssueByCurrentKey("BES-10") // Verfügbares Budget-issue
def spentBudgetIssue = issueManager.getIssueByCurrentKey("BES-19") // verbrauchtes Budget-issue

//get budgets:
def yearly = (Float) yearlyBudgetIssue.getCustomFieldValue(cfBudget)  //Jahres Budget
//def available = availableBudgetIssue.getCustomFieldValue(cfBudget)  //Verfügbares Budget
def spent = (Float) spentBudgetIssue.getCustomFieldValue(cfBudget)  //verbrauchtes Budget

def updatedAvailable = yearly - cumulativeCourseCosts - spent
log.info("current yearly budget " + yearly)
log.info("current spent budget " + spent)
log.info("updated available budget " + updatedAvailable)

availableBudgetIssue.setCustomFieldValue(cfBudget, updatedAvailable)
issueManager.updateIssue(user, availableBudgetIssue, EventDispatchOption.DO_NOT_DISPATCH, false)

//=====================================================================




