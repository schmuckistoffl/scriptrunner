import com.atlassian.jira.event.type.EventDispatchOption
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.component.ComponentAccessor

def issueService = ComponentAccessor.issueService
def customFieldManager = ComponentAccessor.customFieldManager
def issueManager = ComponentAccessor.issueManager


//getting customField objects for field budget and costs
def cfCourseCosts = customFieldManager.getCustomFieldObject("customfield_13302")
def cfBudget = customFieldManager.getCustomFieldObject("customfield_13303")

//get current budget from budgetIssue
MutableIssue budgetIssue = issueManager.getIssueByCurrentKey("BES-10")
def initalBudget = (Integer) budgetIssue.getCustomFieldValue(cfBudget)

//get total cost value from course issue
//def courseIssue = issue.getKey()
//def courseIssue = issueManager.getIssueByCurrentKey(isssue)
def totalCourseCosts = (Integer) issue.getCustomFieldValue(cfCourseCosts)

//calculates new budget subtracting course costs from initial budget
def newBudget = (double) initalBudget - totalCourseCosts

// ===========================================
// only for test reasons
// log.warn("course issue: " + issue)
// log.warn("initial budget: " + initalBudget)
// log.warn("course costs: " + totalCourseCosts)
// log.warn("new budget: " + newBudget)
// ===========================================

//set value of budget field to newly calculated/current budget
budgetIssue.setCustomFieldValue(cfBudget, newBudget)

//update description field with newly calculated budget
budgetIssue.setDescription("""
Verfügbar: *$newBudget*
""")

//getting current logged in user
def loggedInUser = ComponentAccessor.jiraAuthenticationContext.loggedInUser

//update issue
issueManager.updateIssue(loggedInUser, budgetIssue, EventDispatchOption.DO_NOT_DISPATCH, false)
