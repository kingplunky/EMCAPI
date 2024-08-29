# EMCAPI - Plunky's Fork

Welcome to Plunky's fork of the EarthMC API

This version builds upon the original EMCAPI by improving how API data is retrieved. 

The primary focus of this fork is to eliminate the need to request all the data at endpoint 
to determine simple things like which players are online or which players belong to specific nations.

This fork achieves this by allowing responses to be filtered by specific field values.

## New Features

1. [Restructured Query System](#restructured-query-system)
2.  [Informative Error Handling](#informative-error-handling)
3. [Better Responses with `PaginatedResponse<T>`](#better-responses-with-paginatedresponset)

### Restructured Query System


This fork introduces a new class called FilterableEndpoint<T>, which allows for:

- **Generic Filtering**: With `FilterableEndpoint<T>`, you can now filter by any field within the object `T`.
- **Flexible Queries**: The system supports a variety of filtering methods, including:
    - [x] Perform exact value matches for specific fields.
    - [x] Use lists of values to filter by multiple possible values.
    - [x] Apply nested field filtering (e.g, filtering by `Player.status.isOnline`, `Player.nation.name`).
    - [ ] Use regex matching for specific fields (strings, numbers) - _Planned_
    - [ ] Apply field list filtering (e.g, filtering by `Town.outlaws[].name`, `Nation.towns[].name`). - _Planned_
    
#### Example: PlayerEndpoint
Here’s how the `PlayerEndpoint` class, which extends `FilterableEndpoint<Player>`, works in practice:

**1. Simple Filter Query**
- **POST** `/v3/aurora/players?page=1`
```json
{
  "status.isOnline": true
}
```
This will return a list containing all the currently online players

- **Response:** (with missing player fields for simplicity)

```json
{
  "results": [
    {
      "name": "Largeezes",
      "uuid": "6ba4e3bf-93a7-397d-8ebd-1b6ad9e966c4",
      "title": null,
      "surname": null,
      "formattedName": "Largeezes",
      "about": "/res set about [msg]",
      "status": {
        "isOnline": true,
        "isNPC": false,
        "isMayor": false
      }
    }
  ],
  "numResults": 1,
  "numPages": 1,
  "currentPage": 1,
  "pageSize": 100
}
```

**2. Complex Filter Query**

A more complex query can be constructed using multiple filters, including lists of expected values. This allows for highly specific searches.
For example:
- **POST** `/v3/aurora/players?page=1`

```json
{
  "status.isOnline": false,
  "name": ["Largeezes", "Fruitloopins"],
  "nation.name": ["Aland", "Alaska"]
}   
```
This query will return all offline players whose names are either "Largeezes" or "Fruitloopins," and who belong to either the nation "Aland" or "Alaska."

- **Response:** (with missing player fields for simplicity)
```json
{
  "results": [
    {
      "name": "Fruitloopins",
      "uuid": "7b9e0e0f-b1a4-489f-bec3-0b3b2a5e5e1e",
      "title": null,
      "surname": null,
      "about": "CEO of this feature",
      "nation": {
        "name": "Alaska",
        "uuid" : "df84b518-08b3-468e-8e98-6dcb56e0d919"
      },  
      "status": {
        "isOnline": false,
        "isNPC": false,
        "isMayor": false
      }
    },
    {
      "name": "Largeezes",
      "uuid": "6ba4e3bf-93a7-397d-8ebd-1b6ad9e966c4",
      "title": null,
      "surname": null,
      "formattedName": "Largeezes",
      "about": "/res set about [msg]",
      "nation": {
        "name": "Aland",
        "uuid": "e8cfd1b2-1b8e-410b-a5a0-df25d143f4e3"
      },
      "status": {
        "isOnline": false,
        "isNPC": false,
        "isMayor": false
      }
    }
  ],
  "numResults": 2,
  "numPages": 1,
  "currentPage": 1,
  "pageSize": 100
}
```

### Informative Error Handling
Since this new endpoint is a lot more flexible and allows for complex queries like the one above, it is also alot easier to make mistakes
when querying the api. To help users correct these errors, the API provides informative error messages.

For instance, consider the following:

- **POST** `/v3/aurora/players?page=1`
```json
{
    "status.isOnline": "faalse"
}
```
In this case, the user intended to filter players based on their online status but made a typo, resulting in the wrong value type.

- **Response** - `400 - Bad Request`

```json
[
    "Expected type 'boolean' for field path 'Player.status.isOnline'"
]
```
The response will then declare that the expected value for that field was the incorrect type. 

Incorrect field paths will also be flagged:
- **POST** `/v3/aurora/players?page=1`
```json
{
  "nation..dsf..dsf.d": true,
  "invalsid.field": 2,
  "invsalid": ["world", "hello"],
  "nation.as": true,
  "nation.a": true
}
```
- **Response** - `400 - Bad Request`
```json
[
  "Player.nation.as is not a valid field.",
  "Player.nation.a is not a valid field.",
  "Player.nation..dsf..dsf.d is not a valid field.",
  "Player.invalsid.field is not a valid field.",
  "Player.invsalid is not a valid field."
]
```

You will also be informed if you exceed any of the configurable values in your query:
```yaml
filterable_endpoint:
  #The maximum amount of values the user can check for each filter field.
  max_expected_values: 100
  #The maximum amount of field filters the user can use per query.
  max_filters: 5
```
#### **Examples:**

- **POST** `/v3/aurora/players?page=1`
```json
{
    "nation.name": "Aland",
    "name": "Largeezes",
    "status.isOnline": true,
    "town.name": "Anstad",
    "stats.numFriends": 0,
    "permissions.canBuild.resident": true
}
```
This query has total of six filters, when the max is five.
```json
[
    "Invalid json body: Unexpected IOException (of type java.io.IOException): Exceeded maximum number of filters: 5"
]
```

- **POST** `/v3/aurora/players?page=1`
```json
{
  "nation.name": ["Aland", "Alaska", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Venice"]
}
```
The field `nation.name` has been given 101 expected values, when the max is currently 100 per field
```json
[
  "Invalid json body: Unexpected IOException (of type java.io.IOException): Exceeded max expected values for filter object: expected 100 item(s) or less but got 101."
]
```

### Better Responses with `PaginatedResponse<T>`
When querying the api, responses from the `FilterableEndpoint<T>` class are broken into pages (the size of these pages are configurable), allowing the API to handle large queries effectively. 

Because of this, you are able to request a filterable endpoint without specifying filters, allowing the user to easily retrieve a list of all the players on the server, without experiencing timeouts.

#### **Example: Retrieving All Players**
- **POST** `/v3/aurora/players?page=1` - (no json body, as no filter)
- **Response** (with the results array omitted)
```json
{
    "results": [
    ],
    "numResults": 23680,
    "numPages": 237,
    "currentPage": 1,
    "pageSize": 100
}
```
This response indicates that there are 23,680 players in total, spread across 237 pages, with 100 players per page. The `results` array contains the players for the first page.
Now, to retrieve the full list of players, you can simply iterate through the pages by incrementing the page in following requests:

    POST /v3/aurora/players?page=1
    POST /v3/aurora/players?page=2
    POST /v3/aurora/players?page=3
    ...
    POST /v3/aurora/players?page=237

This will give you a completed list of all the players.
