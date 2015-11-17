(ns sandbox.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

; APIのURL(さいたま)
(def +API-URL+ "http://weather.livedoor.com/forecast/webservice/json/v1?city=110010")

; +API-URL+からjsonデータを取得しjson形式で保存する
(defn get-tenki []
  (let [tenki-data (client/get +API-URL+)]
  (json/read-str (:body tenki-data) :key-fn keyword)))

; 今日の天気情報のみを抽出する
(defn get-today-tenki [json-tenki]
  (first (:forecasts json-tenki)))

; 気温を取得する
; (最高気温) / (最低気温)
(defn get-today-temperature [forecasts]
  (str (:celsius (:max (:temperature forecasts))) "/" (:celsius (:min (:temperature forecasts)))) )

(def webhook-url "")

(defn send-to-slack [text]
  (client/post webhook-url { :headers {"Content-Type" "application/json"}
                             :body (json/write-str
                               {
                                 :username "すごいことを教えてくれる"
                                 :text text
                                 :icon_emoji ":underage:"
                                 })}))

; 天気を表示させるメイン
(defn -main [& args]
  (let [today (get-today-tenki (get-tenki))]
    (send-to-slack
      (str "さいたま\n" (:date today) "\n今日の天気: " (:telop today) "\n気温(最高/最低): " (get-today-temperature today))) ))
