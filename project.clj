(defproject io.logicblocks/derivative "0.1.0-RC0"
  :description "General purpose in place scaffolding and source code rewrite tool."
  :url "https://github.com/logicblocks/derivative"

  :license {:name "The MIT License"
            :url  "https://opensource.org/licenses/MIT"}

  :plugins [[lein-cloverage "1.2.4"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.7.0"]
            [lein-changelog "0.3.2"]
            [lein-cprint "1.3.3"]
            [lein-eftest "0.6.0"]
            [lein-codox "0.10.8"]
            [lein-cljfmt "0.9.2"]
            [lein-kibit "0.1.8"]
            [lein-bikeshed "0.5.2"]
            [jonase/eastwood "1.4.0"]]

  :dependencies [[hbs "1.0.3"]
                 [camel-snake-kebab "0.4.3"]
                 [io.logicblocks/pathological "0.1.22-RC2"]
                 [secure-rand "0.1"]]

  :profiles
  {:shared
  ^{:pom-scope :test}
  {:dependencies
   [[org.clojure/clojure "1.11.1"]

    [com.google.jimfs/jimfs "1.3.0"]
    [org.mockito/mockito-core "5.4.0"]

    [eftest "0.6.0"]]}

   :dev
   [:shared {:source-paths ["dev"]
             :eftest       {:multithread? false}}]

   :test
   [:shared {:eftest {:multithread? false}}]

   :unit
   [:shared {:test-paths ^:replace ["test/unit"]
             :eftest     {:multithread? false}}]
   :integration
   [:shared {:test-paths ^:replace ["test/integration"]
             :eftest     {:multithread? false}}]

   :prerelease
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]]}

   :release
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["codox"]
     ["changelog" "release"]
     ["shell" "sed" "-E" "-i.bak" "s/\"[0-9]+\\.[0-9]+\\.[0-9]+\"/\"${:version}\"/g" "README.md"]
     ["shell" "rm" "-f" "README.md.bak"]
     ["shell" "git" "add" "."]
     ["vcs" "commit" "Release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]
     ["change" "version" "leiningen.release/bump-version" "patch"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["vcs" "push"]]}}

  :test-paths ["test/unit" "test/integration"]

  :target-path "target/%s/"

  :eftest {:multithread? false}

  :cloverage
  {:ns-exclude-regex [#"^user"]}

  :codox
  {:namespaces  [#"^derivative\."]
   :metadata    {:doc/format :markdown}
   :output-path "docs"
   :source-uri  "https://github.com/logicblocks/derivative/blob/{version}/{filepath}#L{line}"}

  :cljfmt {:indents ^:replace {#".*" [[:inner 0]]}}

  :eastwood {:config-files ["config/linter.clj"]}

  :deploy-repositories
  {"releases"  {:url "https://repo.clojars.org" :creds :gpg}
   "snapshots" {:url "https://repo.clojars.org" :creds :gpg}}

  :aliases {"test" ["do"
                    ["with-profile" "unit" "eftest" ":all"]
                    ["with-profile" "integration" "eftest" ":all"]]})
