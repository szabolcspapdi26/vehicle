podman compose up -d

podman exec -it kafka kafka-topics --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 3 --topic input
podman exec -it kafka kafka-topics --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 3 --topic output --config cleanup.policy=compact --config min.cleanable.dirty.ratio=0.01 --config max.compaction.lag.ms=100

until $(curl --output /dev/null --silent --head --fail http://localhost:8081/subjects); do
    printf 'waiting to schema-registry '
    sleep 5
done

  curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
    --data '{
      "schema": "{\"$schema\": \"http://json-schema.org/draft-04/schema#\", \"title\": \"Coordinate\", \"type\": \"object\", \"properties\": {\"x\": {\"type\": \"number\"}, \"y\": {\"type\": \"number\"}}, \"required\": [\"x\", \"y\"]}",
      "schemaType": "JSON"}' \
    http://localhost:8081/subjects/input-value/versions