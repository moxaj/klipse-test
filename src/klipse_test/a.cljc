(ns klipse-test.a
  #?(:cljs (:require-macros [klipse-test.a])))

(defmacro x [sym]
  `(def ~sym 10))
