(ns homework.core-spec
  (:require [speclj.core :refer :all]
            [homework.core :refer :all]))

(defn is-the-root? [node]
  (= (:contents node) "ROOT"))

(describe "dfs"
  (with tree {:contents "ROOT"
              :children [{:contents "FIRST CHILD"
                          :children []}
                         {:contents "SECOND CHILD"
                          :children [{:contents "GRANDCHILD" :children []}]}]})
  (it "finds the root node"
    (should=
      @tree
      (find-node
        is-the-root?;condition for success, "find me the root node"
        @tree)));where to look

  (it "returns nil when the target is absent"
    (should= nil (find-node (fn [_] false) @tree)))

  (it "Finds the second child of the root"
    (let [second-child {:contents "SECOND CHILD"
                         :children [{:contents "GRANDCHILD" :children []}]}]
      (should= second-child (find-node #(= (:contents %) "SECOND CHILD") @tree))))

  (it "Finds the first child of the root"
    (let [first-child {:contents "FIRST CHILD" :children []}]
      (should= first-child (find-node #(= (:contents %) "FIRST CHILD") @tree)))))

;note: 'rest' is a function!
(describe "DFS with Custom child-finder"
  (with tree [2
               [7 [2]]
               [9
                [4 [11] [19]]]
               [22
                [5 [81]]]])

  (it "finds the grandchild with contents of '5'"
    (should= [5 [81]] (find-node-custom #(= (first %) 5) rest @tree))))

(describe "BONUS ROUND: can make assertions on the path it took to get to a node"
  (with tree [1 [9  [2]]
                [5]
                [2  [82 [17]]
                    [3  [11 [99]]
                        [4  [101 [78]]
                            [5   [9000]]]]]
                [77 [11 [5]]]])
  (it "finds the [5] which is found via a path of 1-2-3-4-5"
    (should=
      [5 [9000]]
      (find-node-with-path
        []
        #(= [1 2 3 4 5] (map first %))
        rest
        @tree))))

(describe "find ALL the finds!"
  (with tree {:contents "GRANDMA SALLY"
              :children [{:contents "PARENT JOEY"
                          :children [{:contents "GRANDCHILD DYLAN" :children []}]}
                         {:contents "PARENT JOANNE"
                          :children [{:contents "GRANDCHILD LOGAN" :children []}
                                     {:contents "GRANDCHILD KATIE" :children []}]}]})
  (it "finds all the grandchildren"
    (let [grandchildren #{{:contents "GRANDCHILD DYLAN" :children []}
                          {:contents "GRANDCHILD LOGAN" :children []}
                          {:contents "GRANDCHILD KATIE" :children []}}]
    (should= grandchildren (find-all #(.startsWith (:contents %) "GRANDCHILD") @tree)))))
