(ns derivative.transformations.core)

(defmulti apply-transformation
  (fn [transformation _] (:type transformation)))

#_(defprotocol FileSystemTransformation
    (plan [this configuration options])                       ; => diffs
    (apply [this configuration options])                      ; => updates

    (inputs [this])
    (effects [this]))
