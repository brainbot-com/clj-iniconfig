(ns com.brainbot.iniconfig-test
  (:require [clojure.test :refer :all]
            [com.brainbot.iniconfig :refer :all]))


(deftest test-simple
  (testing "parsing empty section"
    (is (= {"foo" {}} (read-ini-string "[foo]\n"))))
  (testing "trim section name"
    (is (= {"foo" {}}
           (read-ini-string "[  foo  \t ]"))))
  (testing "parsing empty file"
    (is (= {} (read-ini-string "")) "empty string should give empty map"))
  (testing "simple example"
    (is (= {"foo" {"baz" "1" "bar" "18"}}
           (read-ini-string "[foo]\nbaz=1\nbar= 18  \n"))))
  (testing "simple continuation"
    (is (= {"foo" {"baz" "\n  1\n  2"}}
           (read-ini-string "[foo]\nbaz =\n  1\n  2"))))
  (testing "] character in comment"
    (is (= {"foo" {}}
           (read-ini-string "[foo]  # please handle ] # here")))))


(deftest syntax-errors
  (testing "continuation with missing assignment"
    (is (thrown-with-msg?
          Exception
          #"bad continuation"
          (read-ini-string "[foo]\nbaz=1\n[bar]\n  garbage\n"))))

  (testing "continuation with missing assignment"
    (is (thrown-with-msg?
          Exception
          #"bad continuation"
          (read-ini-string "[foo]\n  garbage\n")))))
