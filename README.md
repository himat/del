# tool

generated using Luminus version "4.38"

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start the web server, run:

    lein run 


For the REPL, run:

    lein repl
and then to start the server in the REPL:
    (start)
and mount to access DB 
    (mount/start #'super-browser-tool.db.core/*db*)
then you can start calling functions like `(super-browser-tool.db.core/get-users)`

## Migrations

Create a up.sql file and the down.sql file
`lein migratus create <migration name` e.g. `lein migratus create add-users-table`

To apply the migration:
`lein run migrate`

