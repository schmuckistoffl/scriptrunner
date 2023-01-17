// Enable debugger
import org.apache.log4j.Logger
import org.apache.log4j.Level

def log = Logger.getLogger("com.acme.CreateSubtask")
log.setLevel(Level.DEBUG)

import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.JiraListener
import com.atlassian.jira.issue.IssueManager

//issue that triggeredimport com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.event.JiraListener
import com.atlassian.jira.issue.IssueManager

//issue that triggered the event
def issue = event.issue
log.warn(issue)
log.warn(event)

// get all changes from event
def changeLog = event?.changeLog
log.warn(changeLog)

//getting detailled changes
def changeItems = changeLog.getRelated("ChildChangeItem")
log.warn(changeItems)

def change = changeItems?.find{it.field == "totalCosts"}
log.warn(change)

if(!change){
    log.warn("There are no changes in this event for the field totalCosts")
} else {
    log.warn(change.oldstring)
    log.warn(change.newstring)
}




//custom field name
// def customFieldName = "totalCosts"

// def customFieldManager = ComponentAccessor.customFieldManager

// def cf = customFieldManager.getCustomFieldObjects(issue).findByName(customFieldName)
// assert cf : "Could not find custom fiel with name $customFieldName."
// def customFieldValue = issue.getCustomFieldValue(cf)


// log.warn(customFieldName)
// log.warn(issue)
// log.warn(customFieldValue)

 




//  the event
// def issue = event.issue

// //custom field name
// def customFieldName = "totalCosts"

// def customFieldManager = ComponentAccessor.customFieldManager

// def cf = customFieldManager.getCustomFieldObjects(issue).findByName(customFieldName)
// assert cf : "Could not find custom fiel with name $customFieldName."
// def customFieldValue = issue.getCustomFieldValue(cf)


// log.warn(customFieldName)
// log.warn(issue)
// log.warn(customFieldValue)

 




