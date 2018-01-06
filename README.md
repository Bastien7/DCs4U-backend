# DCs4U-currency-manager
Digital Currency solution for You! (backend part)


## Currency API
Here are the simple entry points of the currency API

### GET
http://localhost:8080/api/currency?id=currencyId

### POST
http://localhost:8080/api/currency

Request body:
```
{
  "name": "JoeCoin",
  "symbol": "J$",
  "owner": {
    "firstName": "Joe",
    "lastName": "White",
    "email": "joe.white@gmail.com",
    "birthday": "1970-01-01"
  }
}
```

## Transaction API
Here are the simple entry points of the transaction API.
Once you have created a currency, you can do some transaction with it.

### GET
http://localhost:8080/api/transaction?id=transactionId

### POST
http://localhost:8080/api/transaction

Request body:
```
{
  "currencyId": "5a4ff031c5898844546b36bb",
  "quantity": 12,
  "additionalInformation": "Christmas gift for Joe"
}
```

The field *additionalInformation* is **not** mandatory.
