import com.atlassian.jira.component.ComponentAccessor

def cfManager = ComponentAccessor.getCustomFieldManager()

//
def totalCostsField = cfManager.getCustomFieldObject("customfield_13302")
def costsField = cfManager.getCustomFieldObject("customfield_11287")

// defines the issue where customfield will be selected
def issue = ComponentAccessor.issueManager.getIssueByCurrentKey("SRT-10")

// gets the value of the defined customField in defined issue
def valueTotalCosts = (int) issue.getCustomFieldValue(totalCostsField)
def valueCosts = (int) issue.getCustomFieldValue(costsField)

def realCosts = valueTotalCosts - valueCosts

log.warn(valueTotalCosts)
log.warn(valueCosts)
log.warn("Real costs are: "+ realCosts)

//def costsInt = valueCosts.toInteger()
//log.warn(costsInt)