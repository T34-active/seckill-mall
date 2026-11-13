$ErrorActionPreference = "Stop"
$base = "http://127.0.0.1:8080"

Write-Host "== 1) 登录 ==" -ForegroundColor Cyan
$login = Invoke-RestMethod -Method Post -Uri "$base/api/auth/login" `
  -ContentType "application/json" `
  -Body '{"username":"demo","password":"123456"}'
if ($login.code -ne 0) { throw "login failed: $($login | ConvertTo-Json -Compress)" }
$token = $login.data.token
Write-Host "token=$token"

Write-Host "== 2) 当前用户 ==" -ForegroundColor Cyan
$me = Invoke-RestMethod -Uri "$base/api/auth/me" -Headers @{ Authorization = "Bearer $token" }
$me | ConvertTo-Json -Compress

Write-Host "== 3) 商品列表 ==" -ForegroundColor Cyan
$products = Invoke-RestMethod -Uri "$base/api/products"
$products | ConvertTo-Json -Depth 5
$productId = $products.data[0].id
$beforeStock = $products.data[0].redisStock

Write-Host "== 4) 下单 productId=$productId ==" -ForegroundColor Cyan
$order = Invoke-RestMethod -Method Post -Uri "$base/api/orders" `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body (@{ productId = $productId; quantity = 1 } | ConvertTo-Json)
$order | ConvertTo-Json -Depth 5
if ($order.code -ne 0) { throw "create order failed" }

Write-Host "== 5) 我的订单 ==" -ForegroundColor Cyan
$orders = Invoke-RestMethod -Uri "$base/api/orders" -Headers @{ Authorization = "Bearer $token" }
$orders | ConvertTo-Json -Depth 5

Write-Host "== 6) 校验库存减少 ==" -ForegroundColor Cyan
$after = Invoke-RestMethod -Uri "$base/api/products/$productId"
$afterStock = $after.data.redisStock
Write-Host "stock before=$beforeStock after=$afterStock"
if ($afterStock -ne ($beforeStock - 1)) {
  throw "stock not deducted correctly"
}

Write-Host "== 7) 未登录应 401 ==" -ForegroundColor Cyan
$unauth = Invoke-RestMethod -Method Post -Uri "$base/api/orders" `
  -ContentType "application/json" `
  -Body '{"productId":1,"quantity":1}'
if ($unauth.code -ne 401) { throw "expected 401, got $($unauth.code)" }
$unauth | ConvertTo-Json -Compress

Write-Host "`nSMOKE TEST PASSED" -ForegroundColor Green
