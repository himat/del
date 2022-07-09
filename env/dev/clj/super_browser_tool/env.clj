(ns super-browser-tool.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [super-browser-tool.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[super-browser-tool started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[super-browser-tool has shut down successfully]=-"))
   :middleware wrap-dev})
