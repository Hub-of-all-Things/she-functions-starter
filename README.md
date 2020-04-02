# HAT SHE Function Template

## Introduction
This repository contains a Scala template to develop SHE functions.

## Step 1 - Development
1. Locate the method `doExecute` in the class `HelloFunction` and replace the method with your algorithm
    * the `data` input to this method has a specific structure which you can retrieve https://api.hubofallthings.com/?version=latest#ce6c20b9-9897-47af-8fec-e679f89ad0ad
    * the return object should be a `play.api.libs.json.JsObject` as defined here -> https://www.playframework.com/documentation/2.7.x/api/scala/play/api/libs/json/JsObject.html
2. Refactor your class name and variables. Search `*.scala, *.sbt, serverless.yml, build.sh` files for the following text
    * *HelloFunction*
    * *helloFunction*
    * *hello-function*

These steps above are sufficient for you to have developed the SHE function to a state sufficient for testing and submission for approval.
After testing your function (see `Step 2 - Testing` below), and submitting your function for integration into the HAT, we will update the
`configuration` object and the `dataBundle` definition in the main `HelloFunction` class (see lines 21 and 23). The information used to 
do the update will be obtained from your `Tool Integration Form`. (Of course, you have the option of editing the `configuration` and `dataBundle`
definitions yourself.)


## Step 2 - Testing
1. Deploy the function you have coded above into your AWS Lambda. This template kit makes use of the `serverless` [https://serverless.com] toolkit.
   Make sure you have edited your `serverless.yml` and `build.sh`.
   * Just run `build.sh`
   
   A successful deployment will report something like this.
```Serverless: Stack update finished...
Service Information
service: hello-world-service
stage: dev
region: eu-west-1
stack: hello-world-service-dev
resources: 26
api keys:
  None
endpoints:
  POST - https://6umdgqg102.execute-api.eu-west-1.amazonaws.com/dev/hello-function/1.0.0
  GET - https://6umdgqg102.execute-api.eu-west-1.amazonaws.com/dev/hello-function/1.0.0/configuration
  GET - https://6umdgqg102.execute-api.eu-west-1.amazonaws.com/dev/hello-function/1.0.0/data-bundle
functions:
  api-hello-function: hello-world-service-dev-api-hello-function
  api-hello-function-configuration: hello-world-service-dev-api-hello-function-configuration
  api-hello-function-bundle: hello-world-service-dev-api-hello-function-bundle
  hello-function: hello-world-service-dev-hello-function
  hello-function-configuration: hello-world-service-dev-hello-function-configuration
  hello-function-bundle: hello-world-service-dev-hello-function-bundle
layers:
  None   
```

2. We have a Postman collection to help test your SHE function -> `https://documenter.getpostman.com/view/110376/RWTiveWS`

3. Testing Steps using the above deployment as example
    * Load the function configuration with `GET https://6umdgqg102.execute-api.eu-west-1.amazonaws.com/dev/hello-world/1.0.0/configuration`
        - refer to `https://documenter.getpostman.com/view/110376/RWTiveWS?version=latest#eef329f6-76ad-4791-8d64-6fe33b8d9afa` for more details
        - Let's call the output of this action as `sheFunctionConfiguration`
    * Using an existing HAT with the data you want, create a `data-bundle` of the data you want.
        - Login to the HAT `https://documenter.getpostman.com/view/110376/RWTiveWS?version=latest#89a7cec1-48f2-48ee-b73a-af76f091fc76`
        - Create the `data-bundle` -> `https://docs.dataswift.io/guides/data-bundling`
        - Retrieve the `values` of the `data-bundle` -> `https://documenter.getpostman.com/view/110376/RWTiveWS?version=latest#af0d6b79-dd06-4de6-99c9-1dfa571a85d0`
        - NB: **When you create a `data-bundle` with the name `hello`, you must retrieve it with the same name**
        ```
        # Creating the bundle with the name hello
        POST https://testing.hubat.net/api/v2/data-bundle/hello
        
        # Retrieving the values defined by the above bundle
        GET https://testing.hubat.net/api/v2/data-bundle/hello/values
        ``` 
        - Let's call the output of this action the `dataBundle`
        
    * Call the SHE function you have developed with the `sheFunctionConfiguration` and `bundleData`
        ```
        POST - https://6umdgqg102.execute-api.eu-west-1.amazonaws.com/dev/hello-function/1.0.0
        
        data =
        {
        	"functionConfiguration": {{sheFunctionConfiguration}},
        	"request": {
        		"data": {{bundleData}},
        		"linkRecords": true
        	}
        }
        ```
        
    * If you do not have existing HAT with the data you want to test with, you can write some test data to the HAT
    with `https://docs.dataswift.io/guides/raw-data-io` and then follow the above test sequence.