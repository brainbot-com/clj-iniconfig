(ns com.brainbot.iniconfig-test
  (:require [clojure.test :refer :all]
            [com.brainbot.iniconfig :refer :all]))


(deftest test-simple
  (testing "comment character"
    (is (= {"foo" {"bar" "\n    1 # bar"}} (read-ini-string "[foo]\nbar =\n    1 # bar")))
    (is (= {"foo" {"bar" "1 # bar"}} (read-ini-string "[foo]\nbar = 1 # bar"))))
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


(deftest usage-errors
  (testing "duplicate assignment"
    (is (thrown-with-msg?
         Exception
         #"duplicate assignment"
         (read-ini-string "[foo]\nbaz=1\nbar=5\nbaz=1\n"))))

  (testing "duplicate section name"
    (is (thrown-with-msg?
         Exception
         #"duplicate section"
         (read-ini-string "[foo]\n[foo]\nbaz=1\n")))))


(deftest syntax-errors
  (testing "assignment before section"
    (is (thrown-with-msg?
         Exception
         #"assignment"
         (read-ini-string "baz=1\n"))))

  (testing "empty section name"
    (is (thrown-with-msg?
         Exception
         #"empty section name"
         (read-ini-string "[  ]\nbaz=1\n"))))
  (testing "continuation with missing assignment"
    (is (thrown-with-msg?
          Exception
          #"bad continuation in line 4"
          (read-ini-string "[foo]\nbaz=1\n[bar]\n  garbage\n"))))

  (testing "cannot parse line"
    (is (thrown-with-msg?
          Exception
          #"cannot parse"
          (read-ini-string "[foo]\n=baz\n"))))
  (testing "continuation with missing assignment"
    (is (thrown-with-msg?
          Exception
          #"bad continuation"
          (read-ini-string "[foo]\n  garbage\n")))))
