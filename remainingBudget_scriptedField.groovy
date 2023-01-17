import com.atlassian.jira.jql.parser.JqlQueryParser
import com.atlassian.jira.web.bean.PagerFilter
import com.atlassian.jira.bc.issue.search.SearchService
import com.atlassian.jira.component.ComponentAccessor

def issueService = ComponentAccessor.issueService
def searchService = ComponentAccessor.getComponent(SearchService)
def jqlQueryParser = ComponentAccessor.getComponent(JqlQueryParser)
def query = jqlQueryParser.parseQuery("project = BES and issuetype = course and status = 'Selected for Development'")

def loggedInUser = ComponentAccessor.jiraAuthenticationContext.loggedInUser

def results = searchService.search(loggedInUser, query, PagerFilter.getUnlimitedFilter())

def customFieldManager = ComponentAccessor.customFieldManager
def totalCostsField = customFieldManager.getCustomFieldObjectsByName("totalCosts").first()

// iterating over jql result
results.results.each { issue ->
    def totalCosts = issue.getCustomFieldValue(totalCostsField)
    log.warn("Looking at issue $issue.key with total costs of $totalCosts")
    
}
