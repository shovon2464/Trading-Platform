import React, { useEffect, useRef, useState } from "react";
import { createChart, ColorType } from "lightweight-charts";
import { Colors } from "./Colors";
import { Client } from "@stomp/stompjs";
import { calculatePriceChange } from "./Utils";
import { useLocation, BrowserRouter as Router } from "react-router-dom";

function App() {
  const chartContainerRef = useRef();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const theme = queryParams.get("theme") === "dark" ? "dark" : "light";
  const stock = queryParams.get("stock") || "ZOMATO";

  const [stockData, setStockData] = useState(null);
  const initialRef = useRef(false);

  useEffect(() => {
    const handleResize = () => {
      if (chart) {
        chart.applyOptions({
          width: chartContainerRef.current.clientWidth,
          handleScroll: chart.timeScale().scrollToRealTime(),
        });
      }
    };

    const chart = createChart(chartContainerRef.current, {
      layout: {
        background: {
          type: ColorType.Solid,
          color: theme === "dark" ? Colors.dark_background : Colors.light_background,
        },
        textColor: theme === "dark" ? Colors.dark_text : Colors.light_text,
      },
      crosshair: { mode: 2 },
      grid: {
        horzLines: {
          color: theme === "dark" ? Colors.dark_border : Colors.light_border,
          visible: true,
        },
        vertLines: {
          visible: true,
          color: theme === "dark" ? Colors.dark_border : Colors.light_border,
        },
      },
      width: chartContainerRef.current.clientWidth,
      height: window.innerHeight,
    });

    const candleSeries = chart.addCandlestickSeries({
      upColor: Colors.profit,
      borderUpColor: Colors.profit,
      wickUpColor: Colors.profit,
      downColor: Colors.loss,
      borderDownColor: Colors.loss,
      wickDownColor: Colors.loss,
    });

    chart.timeScale().scrollToPosition(5);
    chart.timeScale().applyOptions({ timeVisible: true });

    // STOMP client — connects to Spring WebSocket natively
    const stompClient = new Client({
      brokerURL: "ws://localhost:8080/ws",
      onConnect: () => {
        console.log("Connected to server");

        // Tell server which stock to subscribe to
        stompClient.publish({
          destination: "/app/subscribeToStock",
          body: stock,
        });

        // Listen for stock updates on /topic/{symbol}
        stompClient.subscribe(`/topic/${stock}`, (message) => {
          console.log("RAW MESSAGE:", message.body);
          
          const data = JSON.parse(message.body);
          console.log("dayTimeSeries length:", data.dayTimeSeries?.length);
          
          const convertedData = data.dayTimeSeries || [];
          setStockData(data);

          if (!initialRef.current && convertedData.length > 0) {
            candleSeries.applyOptions({
              priceLineStyle: 1,
              baseLineStyle: 1,
            });
            candleSeries.setData(convertedData);
            initialRef.current = true;
          }

          if (convertedData.length > 0) {
            const updateValue = convertedData[convertedData.length - 1];
            candleSeries.update({
              time: updateValue.time,
              close: updateValue.close,
              high: updateValue.high,
              low: updateValue.low,
              open: updateValue.open,
              _internal_originalTime: updateValue.time,
            });
          }
        });
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame);
      },
      onWebSocketError: (event) => {
        console.error("WebSocket error:", event);
      },
      onDisconnect: () => {
        console.log("Disconnected from server");
      }
    });

    stompClient.activate();

    window.addEventListener("resize", handleResize);
    
    return () => {
      stompClient.deactivate();
      window.removeEventListener("resize", handleResize);
      chart.remove();
    };
  }, [stock, theme]); // Re-run if stock or theme changes

  return (
    <div>
      {stockData && stockData?.length !== 0 && (
        <div style={styles}>
          <h2
            style={{
              color: theme === "dark" ? Colors.dark_text : Colors.light_text,
              fontWeight: 400,
            }}
          >
            {stock} . 1 . RSE
          </h2>
          <h2
            style={{
              color: calculatePriceChange(
                stockData.lastDayTradedPrice,
                stockData.currentPrice
              ).isPositive
                ? Colors.profit
                : Colors.errorColor,
              fontWeight: 500,
              fontSize: "16px",
            }}
          >
            {stockData &&
              calculatePriceChange(
                stockData.lastDayTradedPrice,
                stockData.currentPrice
              ).currentPrice +
                " (" +
                calculatePriceChange(
                  stockData.lastDayTradedPrice,
                  stockData.currentPrice
                ).percentageChange +
                ")"}
          </h2>
        </div>
      )}
      <div ref={chartContainerRef} />
    </div>
  );
}

function AppWithRouter() {
  return (
    <Router>
      <App />
    </Router>
  );
}

export default AppWithRouter;

const styles = {
  position: "absolute",
  left: "12px",
  top: "12px",
  zIndex: 99,
  lineHeight: "18px",
  fontWeight: 300,
};