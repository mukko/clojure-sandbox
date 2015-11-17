;ツェラーの公式
(ns sandbox.zeller
  (:gen-class)
  (:require [clj-time.core :as time]
            [clj-time.format :as tf]
            [clj-time.local :as tl]))

;現在の年月日を取得
(def tmp-y (Integer/parseInt (tf/unparse (tf/formatter-local "yyyy") (tl/local-now))))
(def tmp-m (Integer/parseInt (tf/unparse (tf/formatter-local "MM") (tl/local-now))))
(def d (Integer/parseInt (tf/unparse (tf/formatter-local "dd") (tl/local-now))))

;年月日の調整をする。例外は考慮しないで書いていることに注意

;1月か2月なら13月14月に変更する
;最終的な月はmに束縛する
(defn month-check [mm]
  (case mm
    1 (def m 13)
    2 (def m 14)
    (def m mm)))

;13月か14月なら年を前年にする
;最終的な年はyに束縛する
(defn year-check [yyyy mm]
  (case mm
    13 (def y (- yyyy 1))
    14 (def y (- yyyy 1))
    (def y yyyy)))
