#!/usr/bin/env python3

import argparse
import bcrypt

def main():
    parser = argparse.ArgumentParser(
        description="Generate a password hashed with bcrypt."
    )
    parser.add_argument(
        "password",
        help="Password to be hashed."
    )
    args = parser.parse_args()

    password_bytes = args.password.encode("utf-8")
    hashed = bcrypt.hashpw(password_bytes, bcrypt.gensalt())
    print(hashed.decode("utf-8"))

if __name__ == "__main__":
    main()
