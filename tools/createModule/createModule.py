#!/usr/bin/env python3

import argparse
import os

import jinja2

parser = argparse.ArgumentParser(description='Provide a name for this module. All the classes and folders will be automatically created.')
parser.add_argument('name', metavar="NAME", help='The name of the module being creatd')

args = parser.parse_args()
name = args.name
print(f"Generating module {name}")
implFolder = f"{name}{os.sep}implementation"
print(f"Creating ${implFolder}")
os.makedirs(implFolder)

