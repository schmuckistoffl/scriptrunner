import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.issue.search.SearchException
import com.atlassian.jira.web.bean.PagerFilter
import org.apache.log4j.Level

import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.component.ComponentAccessor

def issueService = ComponentAccessor.issueService
def customFieldManager = ComponentAccessor.customFieldManager
def issueManager = ComponentAccessor.issueManager

// Set log level to INFO
log.setLevel(Level.INFO)

//define custom field "Total course costs"
def cfCourseCosts = customFieldManager.getCustomFieldObject("customfield_13302")

//define customfield "budget"
def cfBudget = customFieldManager.getCustomFieldObject("customfield_13303")

//get issue to be updated (Budget Subtask)
MutableIssue budgetIssue = issueManager.getIssueByCurrentKey("BES-14")

// The JQL query you want to search with
final jqlSearch = "project = BES and issuetype=Course"

// Some components
def user = ComponentAccessor.jiraAuthenticationContext.loggedInUser
def searchService = ComponentAccessor.getComponentOfType(SearchService)

// Parse the query
def parseResult = searchService.parseQuery(user, jqlSearch)
if (!parseResult.valid) {
    log.error('Invalid query')
    return null
}

// test:
//List costArray = []

try {
    // Perform the query to get the issues
    def cumulativeCourseCosts = 0
    def results = searchService.search(user, parseResult.query, PagerFilter.unlimitedFilter)
    def issues = results.results
    // log.info("result: " + issues)
    issues.each {
        // log.info(it.key)
        def costs = (Float) it.getCustomFieldValue(cfCourseCosts)
        // log.info(costs)
        cumulativeCourseCosts = cumulativeCourseCosts + costs
        // log.info("current cumulative: " + cumulativeCourseCosts)
        
       // costArray.add(it.key)
        
    }
	log.info("final calculation of course costs: " + cumulativeCourseCosts)
    // issues*.key
    //set value of budget field to newly calculated/current budget
	budgetIssue.setCustomFieldValue(cfBudget, cumulativeCourseCosts)
    
    //update issue
	issueManager.updateIssue(user, budgetIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
    //log.info(costArray)
    
} catch (SearchException e) {
    e.printStackTrace()
    null
}
