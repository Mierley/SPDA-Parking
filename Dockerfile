FROM ubuntu:latest
LABEL authors="Mirel"

ENTRYPOINT ["top", "-b"]