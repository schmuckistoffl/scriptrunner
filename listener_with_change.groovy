import com.atlassian.jira.component.ComponentAccessor

//def cfManager = ComponentAccessor.getCustomFieldManager()
def cfManager = ComponentAccessor.getCustomFieldManager()
//
def totalCostsField = cfManager.getCustomFieldObject("customfield_13302")
def costsField = cfManager.getCustomFieldObject("customfield_10810")

// defines the issue where customfield will be selected
def course = event.issue
log.warn("Course issue/trigger: " + course)
def budget = ComponentAccessor.issueManager.getIssueByCurrentKey("BES-1")

// gets the value of import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.user.ApplicationUser
import com.atlassian.jira.event.type.EventDispatchOption

//def cfManager = ComponentAccessor.getCustomFieldManager()

//
def totalCostsField = cfManager.getCustomFieldObject("customfield_13302")
def costsField = cfManager.getCustomFieldObject("customfield_10810")

// defines the issue where customfield will be selected
def course = event.issue as MutableIssue
def isCourse = course.issueType.name == "Course"
log.warn("Course issue/trigger: " + course)
log.warn(isCourse)
def budget = ComponentAccessor.issueManager.getIssueByCurrentKey("BES-1")

log.warn(course)

//test update description of course issue
course.setDescription("test")

def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
ComponentAccessor.getIssueManager().updateIssue(user, course, EventDispatchOption.ISSUE_UPDATED, false)

// gets the value of the defined customField in defined issue
def valueTotalCosts = (int) budget.getCustomFieldValue(totalCostsField)
def valueCosts = (int) course.getCustomFieldValue(costsField)

def realCosts = valueTotalCosts - valueCosts

log.warn("###############################")

log.warn(valueTotalCosts)
log.warn(valueCosts)
log.warn("Real costs are: "+ realCosts)

log.warn("###############################")

// get all changes from event
def changeLog = event?.changeLog
log.warn(changeLog)

//getting detailled changes
def changeItems = changeLog.getRelated("ChildChangeItem")
log.warn("changeitems: " + changeItems)

def change = changeItems?.find{it.field == "costs"}
log.warn(change)

if(!isCourse){
    log.warn("trigger issue $course is not of type course")
}
else if(!change){
    log.warn("There are no changes in this event for the field costs and course issue")
} else {
    log.warn(change.oldstring)
    log.warn(change.newstring)
  //  course.setDescription("Test")
}

log.warn("###############################")


//def costsInt = valueCosts.toInteger()
//log.warn(costsInt) defined customField in defined issue
def valueTotalCosts = (int) budget.getCustomFieldValue(totalCostsField)
def valueCosts = (int) course.getCustomFieldValue(costsField)

def realCosts = valueTotalCosts - valueCosts

log.warn("###############################")

log.warn(valueTotalCosts)
log.warn(valueCosts)
log.warn("Real costs are: "+ realCosts)

log.warn("###############################")

// get all changes from event
def changeLog = event?.changeLog
log.warn(changeLog)

//getting detailled changes
def changeItems = changeLog.getRelated("ChildChangeItem")
log.warn("changeitems: " + changeItems)

def change = changeItems?.find{it.field == "costs"}
log.warn(change)

if(!change){
    log.warn("There are no changes in this event for the field costs and course issue")
} else {
    log.warn(change.oldstring)
    log.warn(change.newstring)
}

log.warn("###############################")


//def costsInt = valueCosts.toInteger()
//log.warn(costsInt)