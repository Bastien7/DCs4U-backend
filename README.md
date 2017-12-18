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
  name: String, 
  symbol: String,
  owner: {
    firstName: String,
    lastName: String,
    email: String,
    birthday: Date
  }
}
```
