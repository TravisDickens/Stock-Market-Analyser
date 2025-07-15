# Real-Time Stock Market Analyzer 📈

This is a full-stack real-time stock market analysis web application that showcases technical charting, candlestick pattern detection, and algorithmic signal generation. Built to mirror advanced trading platforms like TradingView, it integrates real-time price feeds, technical indicators, and smart signal logic — all using a modern Spring Boot backend and a responsive HTML/JavaScript frontend with LightweightCharts.

## 🔍 Features

- 📊 **Candlestick Charts** with multiple timeframe support (1M, 5M, 15M, 1H, 1D)
- 🔍 **Smart Pattern Scanner**: Detects Doji, Engulfing patterns, Hammers, Morning/Evening Stars, and more
- 🧠 **Trading Signal Engine**: MA crossovers and momentum-based signal generator (Buy/Sell/Hold)
- 🧮 **Multiple Moving Averages** (MA5, MA10, MA20) plotted live
- 📚 **Watchlist**: Track multiple stocks (AAPL, TSLA, etc.) and add your own
- 🔄 **Refresh & Real-Time Updates** (with loading states)
- 🧱 **Spring Boot REST API** backend serving OHLC data to frontend
- ✨ Designed with a clean dark UI

## 🎨 Marker Legend (Candlestick Patterns)

The chart displays colored circle markers representing detected candlestick patterns:

| Color     | Pattern Name          | Meaning / Description                   |
| --------- | --------------------- | --------------------------------------- |
| 🟠 Orange | **Doji**              | Market indecision, possible reversal    |
| 🟢 Green  | **Bullish Engulfing** | Potential bullish reversal              |
| 🔴 Red    | **Bearish Engulfing** | Potential bearish reversal              |
| 🔵 Blue   | **Hammer**            | Bullish reversal at bottom of downtrend |
| 🌸 Pink   | **Shooting Star**     | Bearish reversal at top of uptrend      |
| 🟢 Green  | **Morning Star**      | Bullish 3-candle reversal pattern       |
| 🔴 Red    | **Evening Star**      | Bearish 3-candle reversal pattern       |

## 📈 Moving Averages (MA) Explained

Moving Averages (MAs) are one of the most popular technical indicators used in trading to smooth out price data and help identify trends over different periods.

* **MA5 (5-period Moving Average)**
  Tracks the average price over the last 5 candles. It reacts quickly to recent price changes and is used to capture short-term trends.

* **MA10 (10-period Moving Average)**
  Represents the average price over the last 10 candles. It smooths out more noise than the MA5 and is commonly used to identify intermediate-term trends.

* **MA20 (20-period Moving Average)**
  Calculates the average over 20 candles, showing longer-term trend direction. It reacts slower to price changes and helps confirm major trend shifts.

### How MAs are used in this project:

* Crossovers between these MAs (e.g., MA5 crossing above MA10) often signal potential buy or sell opportunities.
* The different speeds of MAs help indicate whether a stock is trending upward, downward, or moving sideways.
* Combined with momentum analysis, these MAs contribute to the trading signal engine generating **Buy**, **Sell**, or **Hold** recommendations.

## 💡 Motivation

This project was created as a **personal full-stack showcase** to demonstrate:

- Clean architectural design using **Spring Boot** for backend services
- **JavaScript + LightweightCharts** for interactive financial charting
- Ability to integrate **technical analysis** into web apps
- Frontend/backend interaction through RESTful APIs
- Interface design principles


## 🛠️ Technologies Used

- **Frontend**: HTML, CSS, JavaScript, LightweightCharts
- **Backend**: Java Spring Boot (REST APIs)
- **Data**: OHLC data retrieved using Twelve Data API
- **Design**: Fully responsive UI with modular chart and control system


## 📌 Disclaimers

> ⚠️ **This project is for educational and demonstration purposes only.**
> It does **not constitute financial advice**, and **no liability** is accepted for how this code or its outputs are used in any environment.


## Contribution

This project is **not open for external contributions**.


## 🔒 Notice

This is a personal project to showcase development skills and technical knowledge.
**Do not use this project for actual trading decisions or investment strategies.**


