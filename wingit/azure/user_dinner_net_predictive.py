import urllib2
# If you are using Python 3+, import urllib instead of urllib2

import json 


data =  {

        "Inputs": {

                "input1":
                {
                    "ColumnNames": ["American", "French", "Italian", "Spanish", "Indian", "Asian", "Day", "Time", "Target"],
                    "Values": [ [ "0", "0", "0", "0", "0", "0", "Monday", "0", "?" ], [ "0", "0", "0", "0", "0", "0", "Monday", "0", "?" ], ]
                },        },
            "GlobalParameters": {
}
    }

body = str.encode(json.dumps(data))

url = 'https://ussouthcentral.services.azureml.net/workspaces/13a9ddfe79eb4f84bab53d2d78a0fefa/services/835cf31d58674ce1adbdeb85dc9f69f8/execute?api-version=2.0&details=true'
api_key = 'zTbiIFzMz2ME2PqB6Zid5AevFq5my5NhP/YrNjUM+kDpoYYMm8KgwT1O8QzTQdPfCwNmDBLkFrkHXEavn+sC2w==' # Replace this with the API key for the web service
headers = {'Content-Type':'application/json', 'Authorization':('Bearer '+ api_key)}

req = urllib2.Request(url, body, headers) 

try:
    response = urllib2.urlopen(req)

    # If you are using Python 3+, replace urllib2 with urllib.request in the above code:
    # req = urllib.request.Request(url, body, headers) 
    # response = urllib.request.urlopen(req)

    result = response.read()
    print(result) 
except urllib2.HTTPError, error:
    print("The request failed with status code: " + str(error.code))

    # Print the headers - they include the requert ID and the timestamp, which are useful for debugging the failure
    print(error.info())

    print(json.loads(error.read()))                              