# -*- coding: utf-8 -*-
import subprocess
import sys

MYSQL = r"C:\dev\mysql-8.4.9-winx64\bin\mysql.exe"
SQL_FILE = r"scripts\add-comments.sql"

with open(SQL_FILE, "r", encoding="utf-8") as f:
    sql = f.read()

apply = subprocess.run(
    [MYSQL, "-uroot", "-p123456", "--default-character-set=utf8mb4"],
    input=sql.encode("utf-8"),
    stdout=subprocess.PIPE,
    stderr=subprocess.PIPE,
)
sys.stdout.buffer.write(apply.stdout)
sys.stderr.buffer.write(apply.stderr)
if apply.returncode != 0:
    sys.exit(apply.returncode)

check_sql = (
    "SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.TABLES "
    "WHERE TABLE_SCHEMA='seckill_mall';"
    "SELECT TABLE_NAME, COLUMN_NAME, COLUMN_COMMENT FROM information_schema.COLUMNS "
    "WHERE TABLE_SCHEMA='seckill_mall' ORDER BY TABLE_NAME, ORDINAL_POSITION;"
)
check = subprocess.run(
    [MYSQL, "-uroot", "-p123456", "--default-character-set=utf8mb4", "-e", check_sql],
    stdout=subprocess.PIPE,
    stderr=subprocess.PIPE,
)
sys.stdout.buffer.write(check.stdout)
sys.stderr.buffer.write(check.stderr)
sys.exit(check.returncode)
