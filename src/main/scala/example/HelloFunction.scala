package example

import org.hatdex.hat.api.models._
import org.hatdex.hat.api.models.applications.{ApplicationDeveloper, ApplicationGraphics}
import org.hatdex.hat.she.functions.SHEModels.{FunctionConfiguration, FunctionInfo, FunctionStatus, FunctionTrigger, Request, Response}
import org.joda.time.{DateTime, Period}
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.{JsObject, Json}


object Client {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
}

class HelloFunction {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val namespace = "she"
  val endpoint = "insights/activity-records"

  val configuration: FunctionConfiguration = FunctionConfiguration(
    "hello-function",
    FunctionInfo(
      "1.0.0",
      new DateTime("2019-01-01T12:00:00+00:00"),
      None,
      "Weekly Summary",
      "A summary of your week’s digital activities",
      FormattedText(
        text = """Weekly Summary shows your weekly online activities.
                 |It allows you to to have an overview of your data accumulated in a week. The first weekly summary establish the start date of the tool and is a summary of your history of activities.""".stripMargin,
        None, None),
      "https://hatdex.org/terms-of-service-hat-owner-agreement",
      "contact@hatdex.org",
      ApplicationGraphics(
        Drawable(None, "", None, None),
        Drawable(None, "https://github.com/Hub-of-all-Things/exchange-assets/blob/master/insights-activity-summary/logo.png?raw=true", None, None),
        Seq(Drawable(None, "https://github.com/Hub-of-all-Things/exchange-assets/blob/master/insights-activity-summary/screenshot1.jpg?raw=true", None, None), Drawable(None, "https://github.com/Hub-of-all-Things/exchange-assets/blob/master/insights-activity-summary/screenshot2.jpg?raw=true", None, None))),
      Some("/she/feed/hello/function")),
    ApplicationDeveloper("hatdex", "HAT Data Exchange Ltd", "https://hatdex.org", Some("United Kingdom"), None),
    FunctionTrigger.TriggerPeriodic(Period.parse("P1W")),
    dataBundle = bundleFilterByDate(None, None),
    status = FunctionStatus(available = true, enabled = false, lastExecution = None, executionStarted = None))

  protected def dateFilter(fromDate: Option[DateTime], untilDate: Option[DateTime]): Option[FilterOperator.Operator] = {
    val dateTimeFormat: DateTimeFormatter = ISODateTimeFormat.dateTime()
    if (fromDate.isDefined)
      Some(FilterOperator.Between(Json.toJson(fromDate.map(_.toString(dateTimeFormat))), Json.toJson(untilDate.map(_.toString(dateTimeFormat)))))
    else
      None
  }

  protected def dateOnlyFilter(fromDate: Option[DateTime], untilDate: Option[DateTime]): Option[FilterOperator.Operator] = {
    if (fromDate.isDefined) {
      Some(FilterOperator.Between(Json.toJson(fromDate.map(_.toString("yyyy-MM-dd"))), Json.toJson(untilDate.map(_.toString("yyyy-MM-dd")))))
    }
    else {
      None
    }
  }

  def bundleFilterByDate(fromDate: Option[DateTime], untilDate: Option[DateTime]): EndpointDataBundle = {
    EndpointDataBundle("hello-function",
      Map(
        "facebook/feed" → PropertyQuery(List(
          EndpointQuery("facebook/feed", Some(Json.toJson(Map("id" → "id"))),
            dateFilter(fromDate, untilDate).map(f ⇒ Seq(EndpointQueryFilter("created_time", None, f))), None)),
          Some("created_time"), Some("descending"), None)
        ))
  }

  def execute(configuration: FunctionConfiguration, request: Request): Seq[Response] = {
    val results = doExecute(request.data)

    logger.info(s"Running since since ${configuration.status.lastExecution}")

    val response = Response(namespace, endpoint, Seq(results), Seq())

    Seq(response)
  }

  /*
    CHANGE ME
    Put your algorithm here
   */
  def doExecute(data: Map[String, Seq[EndpointData]]): JsObject = {
    // Do something here. like
    // -- CHANGE ME Start
    val totalRecordSize = data.collect {
      case (mappingEndpoint, records) ⇒
        (mappingEndpoint, records.flatMap(_.recordId).toSet.size)
    }

    Json.obj("hello" -> Json.toJson(totalRecordSize))
    // -- CHANGE ME End
  }
}
