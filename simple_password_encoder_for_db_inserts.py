import bcrypt

password = b"test1"
hashed = bcrypt.hashpw(password, bcrypt.gensalt())
print(hashed)