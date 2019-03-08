package example

import com.amazonaws.services.lambda.runtime.Context
import org.hatdex.hat.api.models.EndpointDataBundle
import org.hatdex.hat.she.functions.SHEModels
import org.hatdex.hat.she.functions.SHEModels._
import org.hatdex.serverless.aws.proxy.{ProxyRequest, ProxyResponse}
import org.hatdex.serverless.aws.{AnyContent, LambdaHandler, LambdaProxyHandler}
import org.joda.time.DateTime
import play.api.libs.json.JsObject
import play.api.libs.ws.{DefaultBodyWritables, JsonBodyReadables}

import scala.util.Try

class HelloFunctionHandler extends LambdaHandler[SHEModels.SHERequest, Seq[Response]] with JsonBodyReadables with DefaultBodyWritables {
  private val helloFunction = new HelloFunction()

  override def handle(request: SHEModels.SHERequest, context: Context): Try[Seq[Response]] = {
    Client.logger.info(s"Handling request $request with context $context")
    Try(helloFunction.execute(request.functionConfiguration, request.request))
  }
}

class HelloFunctionConfigurationHandler extends LambdaHandler[AnyContent, FunctionConfiguration] with JsonBodyReadables with DefaultBodyWritables {
  private val helloFunction = new HelloFunction()

  override def handle(request: AnyContent, context: Context): Try[FunctionConfiguration] = {
    Client.logger.info(s"Handling request $request with context $context")
    Try(helloFunction.configuration)
  }
}

class HelloFunctionBundleHandler extends LambdaHandler[JsObject, EndpointDataBundle] with JsonBodyReadables with DefaultBodyWritables {
  private val helloFunction = new HelloFunction()

  override def handle(request: JsObject, context: Context): Try[EndpointDataBundle] = {
    Client.logger.info(s"Handling request $request with context $context")
    Try(helloFunction.bundleFilterByDate(
      (request \ "fromDate").asOpt[String].map(r ⇒ DateTime.parse(r)),
      (request \ "untilDate").asOpt[String].map(r ⇒ DateTime.parse(r))))
  }
}


class HelloFunctionProxyHandler extends LambdaProxyHandler[SHEModels.SHERequest, Seq[Response]] with JsonBodyReadables with DefaultBodyWritables {
  private val helloFunction = new HelloFunction()

  override def handle(request: SHEModels.SHERequest, context: Context): Try[Seq[Response]] = {
    Client.logger.info(s"Handling request $request with context $context")
    Try(helloFunction.execute(request.functionConfiguration, request.request))
  }
}

class HelloFunctionConfigurationProxyHandler extends LambdaProxyHandler[AnyContent, FunctionConfiguration] with JsonBodyReadables with DefaultBodyWritables {
  private val helloFunction = new HelloFunction()

  override def handle(context: Context): Try[FunctionConfiguration] = {
    Client.logger.info(s"Handling request with context $context")
    Try(helloFunction.configuration)
  }
}

class HelloFunctionBundleProxyHandler extends LambdaHandler[ProxyRequest[AnyContent], ProxyResponse[EndpointDataBundle]] with JsonBodyReadables with DefaultBodyWritables {
  private val helloFunction = new HelloFunction()

  override def handle(request: ProxyRequest[AnyContent], context: Context): Try[ProxyResponse[EndpointDataBundle]] = {
    Client.logger.info(s"Handling request with context $context")
    val result = Try(helloFunction.bundleFilterByDate(
      request.queryStringParameters.flatMap(_.get("fromDate").map(r ⇒ DateTime.parse(r))),
      request.queryStringParameters.flatMap(_.get("untilDate").map(r ⇒ DateTime.parse(r)))))

    Try(ProxyResponse(result))
  }
}
