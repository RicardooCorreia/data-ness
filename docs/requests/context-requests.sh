
# Post subscription to CBKA
curl --location --request POST 'localhost:1026/v2/subscriptions' \
--header 'Content-Type: application/json' \
--data-raw '{
  "description": "A subscription to get all changes",
  "subject": {
      "entities": [{"idPattern": ".*"}]
  },
  "notification": {
    "http": {
      "url": "http://cbka:8080/api/v1/context"
    }
  },
  "expires": "2040-01-01T14:00:00.00Z",
  "throttling": 5
}'

# Create an context entity
curl --location --request POST 'localhost:1026/v2/entities' \
--header 'Content-Type: application/json' \
--data-raw '{
  "id": "Room1",
  "type": "Room",
  "temperature": {
    "value": 23,
    "type": "Float"
  },
  "pressure": {
    "value": 720,
    "type": "Integer"
  }
}'

# Updated created entity
curl --location --request PATCH 'localhost:1026/v2/entities/Room1/attrs' \
--header 'Content-Type: application/json' \
--data-raw '{
  "temperature": {
    "value": 67.5,
    "type": "Float"
  },
  "pressure": {
    "value": 3,
    "type": "Float"
  }
}'

# Get all entities
curl --location --request GET 'localhost:1026/v2/entities'

# Get all subscriptions
curl --location --request GET 'localhost:1026/v2/subscriptions'
