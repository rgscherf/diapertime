(defproject clj-diaper "0.1.0-SNAPSHOT"
  :description "Simple tracking for your baby's I/O"
  :url "https://diaperti.me"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]

                 [hiccup "1.0.5"]
                 [reagent "0.6.0"]
                 [reagent-utils "0.2.0"]
                 [compojure "1.5.1"]
                 [yogthos/config "0.8"]
                 [org.clojure/clojurescript "1.9.229"
                  :scope "provided"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.7"
                  :exclusions [org.clojure/tools.reader]]

                 [clj-time "0.12.2"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [cljs-ajax "0.5.8"]
                 [clj-http "2.3.0"]
                 [com.novemberain/monger "3.1.0"]
                 [cheshire "5.1.1"]
                 [digest "1.4.5"]

                 [ring "1.5.0"]
                 [ring-server "0.4.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-ssl "0.2.1"]]

  :plugins [[lein-environ "1.0.2"]
            [lein-cljsbuild "1.1.1"]
            [lein-asset-minifier "0.2.7"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler clj-diaper.handler/app
         :uberwar-name "clj-diaper.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "clj-diaper.jar"

  :main clj-diaper.server

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets {:assets
                   {"resources/public/css/style.min.css" "resources/public/css/style.css"}}

  :cljsbuild
            {:builds {:min
                      {:source-paths ["src/cljs" "env/prod/cljs"]
                       :compiler
                       {:output-to "target/cljsbuild/public/js/app.js"
                        :output-dir "target/uberjar"
                        :optimizations :advanced
                        :pretty-print  false}}
                      :app
                      {:source-paths ["src/cljs" "env/dev/cljs"]
                       :compiler
                       {:main "clj-diaper.dev"
                        :asset-path "/js/out"
                        :output-to "target/cljsbuild/public/js/app.js"
                        :output-dir "target/cljsbuild/public/js/out"
                        :source-map true
                        :optimizations :none
                        :pretty-print  true}}}}

  :figwheel {:http-server-root "public"
             :server-port 3449
             :nrepl-port 7002
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["resources/public/css"]
             :ring-handler clj-diaper.handler/app}

  :profiles {:dev {:repl-options {:init-ns clj-diaper.repl
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :dependencies [[ring/ring-mock "0.3.0"]
                                  [ring/ring-devel "1.5.0"]
                                  [prone "1.1.2"]
                                  [figwheel-sidecar "0.5.8"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.cemerick/piggieback "0.2.2-SNAPSHOT"]
                                  [pjstadig/humane-test-output "0.8.1"]]
                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.8"]]
                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]
                   :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks leiningen.cljsbuild]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true
                       :main clj-diaper.server
                       :cljsbuild {:builds {:app
                                             {:source-paths ["env/prod/cljs"]
                                              :compiler {:optimizations :advanced
                                                         :pretty-print true}}}}}})
