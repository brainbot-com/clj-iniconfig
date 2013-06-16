(ns com.brainbot.iniconfig
  (:import [java.io StringReader])
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]))


(defn- parse-comment-line
  [line]
  (if-let [match (re-find #"^\s*(#.*)?$", line)]
    {:type :comment}))


(defn- parse-continuation-line
  [line]
  ;; (if-let [match (re-find #"^(\s+[^#]*(#.*)?", line)]
  (if-let [match (re-find #"^(\s+[^#]*)(#.*)?", line)]
    {:type :continuation :value (nth match 1)}))
;; (parse-continuation-line "    foobar hello #b:a")


(defn- parse-section-line
  [line]
  (if-let [match (re-find #"^\s*\[([^]]*)\]\s*(#.*)?$" line)]
    {:type :section :name (nth match 1)}))


(defn- parse-assignment-line
  [line]
  (if-let [match (re-find #"^(\S[^=]*)=(.*)$" line)]
    {:type :assignment
     :name (nth match 1)
     :value (nth match 2)}))


(defn- parse-line
  [line]
  (assoc
      (or (parse-comment-line line)
          (parse-continuation-line line)
          (parse-section-line line)
          (parse-assignment-line line)
          {:type :error})
    :lineno 42
    :line line))


(defn- build-map
  [parsed-lines]
  (let [make-section identity
        make-variable identity]
    (loop [lines parsed-lines
           retval {}
           section nil
           variable nil]
      (if-let [line (first lines)]
        (let [type (:type line)]
          (cond
            (= type :comment)
              (recur (rest lines) retval section variable)
            (= type :assignment)
              (let [variable (make-variable (string/trim (:name line)))]
                (recur (rest lines)
                       (assoc-in retval [section variable] (string/trim (:value line)))
                       section
                       variable))
            (= type :continuation)
              (if variable
                (recur (rest lines)
                       (assoc-in retval [section variable]
                                 (string/join "\n" [(get-in retval [section variable]),
                                                    (string/trimr (:value line))]))
                       section
                       variable)
                (throw (Exception. "bad continuation")))

            (= type :section)
              (let [trimmed-name (string/trim (:name line))
                    section-name (make-section trimmed-name)]
                (if (= "" trimmed-name)
                  (throw (Exception. "empty section name"))
                  (recur (rest lines)
                         (assoc retval section-name {})
                         section-name
                         nil)))))
        retval))))


(defn read-ini
  "parse .ini file into a map"
  [in]
  (with-open [reader (io/reader in)]
    (build-map (map parse-line (line-seq reader)))))


(defn read-ini-string
  "parse .ini file from string"
  [s]
  (read-ini (StringReader. s)))
