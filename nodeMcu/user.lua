function connectWifi (wifi_ssid, wifi_password)  
    wifi.setmode(wifi.STATION)
    wifi.sleeptype(wifi.NONE_SLEEP)
    wifi.sta.config(wifi_ssid,wifi_password, 1) 
    wifi.sta.eventMonReg(wifi.STA_IDLE, function() print("Wifi Idle") end)
    wifi.sta.eventMonReg(wifi.STA_CONNECTING, function() print("Wifi Connecting") end)
    wifi.sta.eventMonReg(wifi.STA_WRONGPWD, function() print("Wifi Wrong Password") end)
    wifi.sta.eventMonReg(wifi.STA_APNOTFOUND, function() print("Wifi Ap Not Found") end)
    wifi.sta.eventMonReg(wifi.STA_FAIL, function() print("Wifi Connection Failed") end)
    wifi.sta.eventMonReg(wifi.STA_GOTIP, function() onConnect(wifi_ssid, wifi.sta.getip()) end)
    wifi.sta.eventMonStart()
    wifi.sta.connect()
end

function onConnect(ssid, ip)
    print("Wifi connected! ssid:" .. ssid .. ", ip: " .. ip)
    ws = websocket.createClient()
    ws:on("connection", function(ws) print("Websocket connected") end)
    ws:on("receive", onReceive)
    ws:on("close", function(_, status) print("Websocket closed", status) end)
    ws:connect(websocket_url)
end

function onReceive(_, msg, opcode)
    if(opcode == 1 and msg == "open") then
        triggerDoor()
    end
end

function triggerDoor() 
    print("Door open")
    gpio.write(relayPin, gpio.HIGH)
    mytimer = tmr.create()
    mytimer:register(5000, tmr.ALARM_SINGLE, function() 
        gpio.write(relayPin, gpio.LOW)
        print("Door close") 
    end)
    if not mytimer:start() then print("uh oh") end
end

function setup()
    dofile("config.lua")
    relayPin=8
    gpio.mode(relayPin, gpio.OUTPUT)
    gpio.write(relayPin, gpio.LOW)
    
end

setup()
connectWifi(wifi_ssid, wifi_password)
