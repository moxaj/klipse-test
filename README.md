# klipse-test

**Snippet 1:**

```clojure
(ns foo.bar
  (:require [cljs.js :as cjs]))

(def state (cjs/empty-state))

(defn compile [source opts]
  (cjs/compile-str state source "" opts #(println (:value %))))

(def source-a
  "(ns klipse-test.a
     #?(:cljs (:require-macros [klipse-test.a])))

   (defmacro x [sym]
     `(def ~sym 10))")

(def source-b
  "(ns klipse-test.b
     (:require [klipse-test.a :refer [x]]))

   (x asdf)")

(def deps
  {'klipse-test.a source-a
   'klipse-test.b source-b})

(defn load-ns [opts cb]
  (cb {:lang :clj :source (deps (:name opts))}))

(compile source-a {:eval cjs/js-eval :load load-ns})

(compile source-b {:eval cjs/js-eval :load load-ns})
```

**Snippet 2:**

```clojure
(ns klipse-test.a
  #?(:cljs (:require-macros [klipse-test.a])))

(defmacro x [sym]
  `(def ~sym 10))

(ns klipse-test.b
  (:require [klipse-test.a :refer [x]]))

(x asdf)
```

Visit http://app.klipse.tech/.

- **Scenario 1**: evaluated **Snippet 1**, then evaluate **Snippet 2**. Both compile to _correct_ js.
- **Scenario 2**: evaluated **Snippet 2**, then evaluate **Snippet 1**. Both compile to _incorrect_ js.

_Correct_ js   = `klipse_test.b.asdf = (10);` at the end

_Incorrect_ js = `klipse_test.a.x.call(null,klipse_test.b.asdf);` at the end
