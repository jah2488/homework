(ns homework.core)

(defn- find-or-continue [predicate tree else]
  (when tree (if (predicate tree) tree else)))
;refactoring experiement 4/5 closures
; + remove almost 'redundant' base case conditional from callee
; + reduce nesting in callee
; - "wut?" factor
; - Name is slightly confusing

(defn- search-children [fn meth predicate tree]
  (fn #(meth predicate %) (:children tree)))
;refactoring experiement 3/5 closures
; - too many params
; - fn/meth to vague
; + remove (:children tree) duplication
; + add some clarity

(defn find-node [predicate tree]
  (find-or-continue predicate tree
    (let [node (remove empty? (search-children mapcat find-node predicate tree))]
      (when-not (empty? node) (into {} node)))))

(defn- find-some [predicate tree]
  (if (empty? (:children tree))
    (find-node predicate tree)
    (search-children map find-some predicate tree)))

(defn find-all [predicate tree]
  (set (search-children mapcat find-some predicate tree)))

(defn find-node-custom [predicate get-children tree]
  (find-or-continue predicate tree
    (first (keep #(find-node-custom predicate get-children %) (get-children tree)))))


(defn find-node-with-path [path-so-far path-predicate get-children tree] [5 [9000]]);yay I win
