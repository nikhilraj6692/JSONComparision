# JSONComparision
<b><i>A simple utiltity to compare two json and find the difference between both of them. The utiltiy heavily uses recursion to find difference.</i></b>


<u><b>File 1:</b></u>
```
{
  "PartyRec": "123",
  "PersonIndicator": false,
  "OrgPartyInfo": {
    "PersonInfo": {
      "Name": "Nikhil",
      "Roll": 29,
      "address1": "address"
    },
    "InnerList": [
      {
        "listname": "list2",
        "listDesc": "desc2"
      },
      {
        "listname": "list3",
        "listDesc": "desc3"
      }
    ]
  },
  "DummyList": [
    {
      "listname": "list",
      "listDesc": "desc"
    },
    {
      "listname": "list1",
      "listDesc": "desc1"
    }
  ]
}
```
<u><b>File 2:</b></u>
```
{
  "PartyRec": "1234",
  "PersonIndicator": true,
  "OrgPartyInfo": {
    "PersonInfo": {
      "Name": "Ashwin",
      "Roll": 29,
      "Address": [
        {
          "add1": "bhosari"
        },
        {
          "add2": "vishrantwadi"
        }
      ]
    },
    "InnerList": [
      {
        "listname": "list33"
      }
    ],
    "DummyList": [
      {
        "listClass": "class"
      },
      {
        "listname": "list11",
        "listDesc": "desc11"
      }
    ]
  }
}
```
<u><b>Output</b></u>
```
{
  "PartyRec" : "123/1234(Not Match)",
  "PersonIndicator" : "false/true(Not Match)",
  "OrgPartyInfo" : {
    "PersonInfo" : {
      "Name" : "Nikhil/Ashwin(Not Match)",
      "Roll" : "29/29(Match)",
      "address1" : "address/null(Not Match)",
      "Address" : [ {
        "add1" : "null/bhosari(Not Match)"
      }, {
        "add2" : "null/vishrantwadi(Not Match)"
      } ]
    },
    "InnerList" : [ {
      "listname" : "list2/list33(Not Match)",
      "listDesc" : "desc2/null(Not Match)"
    }, {
      "listname" : "list3/null(Not Match)",
      "listDesc" : "desc3/null(Not Match)"
    } ],
    "DummyList" : [ {
      "listClass" : "null/class(Not Match)"
    }, {
      "listname" : "null/list11(Not Match)",
      "listDesc" : "null/desc11(Not Match)"
    } ]
  },
  "DummyList" : [ {
    "listname" : "list/null(Not Match)",
    "listDesc" : "desc/null(Not Match)"
  }, {
    "listname" : "list1/null(Not Match)",
    "listDesc" : "desc1/null(Not Match)"
  } ]
}
```
