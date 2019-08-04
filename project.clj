(defproject io.logicblocks/derivative "0.0.1-SNAPSHOT"
  :description "General purpose in place scaffolding and source code rewrite tool."
  :url "https://github.com/logicblocks/derivative"

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :dependencies [[hbs "1.0.3"]
                 [camel-snake-kebab "672421b575737c5496b7ddcfb83cf150b0d0bc75"]
                 [io.logicblocks/pathological "0.1.1"]
                 [secure-rand "0.1"]]

  :middleware [lein-git-down.plugin/inject-properties]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]
            [lein-eftest "0.5.8"]
            [lein-codox "0.10.7"]
            [reifyhealth/lein-git-down "0.3.5"]]

  :profiles
  {:shared      {:dependencies   [[org.clojure/clojure "1.10.1"]
                                  [eftest "0.5.8"]]}
   :dev         [:shared {:source-paths ["dev"]}]
   :unit        [:shared {:test-paths ^:replace ["test/unit"]
                          :eftest     {:multithread? false}}]
   :integration [:shared {:test-paths ^:replace ["test/integration"]
                          :eftest     {:multithread? false}}]}
  :test-paths ["test/unit" "test/integration"]

  :eftest {:multithread? false}

  :codox
  {:namespaces  [#"^pathological\."]
   :output-path "docs"
   :source-uri  "https://github.com/logicblocks/derivative/blob/{version}/{filepath}#L{line}"}

  :git-down
  {camel-snake-kebab {:coordinates clj-commons/camel-snake-kebab}}

  :deploy-repositories
  {"releases" {:url   "https://repo.clojars.org" :creds :gpg}}

  :repositories [["public-github" {:url "git://github.com"}]]

  :release-tasks
  [["shell" "git" "diff" "--exit-code"]
   ["change" "version" "leiningen.release/bump-version" "release"]
   ["changelog" "release"]
   ["vcs" "commit"]
   ["vcs" "tag"]
   ["deploy"]
   ["change" "version" "leiningen.release/bump-version"]
   ["vcs" "commit"]
   ["vcs" "tag"]
   ["vcs" "push"]]

  :aliases {"test" ["do"
                    ["with-profile" "unit" "eftest" ":all"]
                    ["with-profile" "integration" "eftest" ":all"]]})
