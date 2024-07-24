podman compose up -d

podman exec -it kafka kafka-topics --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 3 --topic input
podman exec -it kafka kafka-topics --create --bootstrap-server localhost:29092 --replication-factor 1 --partitions 3 --topic output --config cleanup.policy=compact --config min.cleanable.dirty.ratio=0.01 --config max.compaction.lag.ms=100

