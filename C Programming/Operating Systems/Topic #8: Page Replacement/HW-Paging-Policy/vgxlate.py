#! /usr/bin/env python

#
# vgxlate.py - translate valgrind memory trace to list of page numbers
#     output is suitable for input to OSTEP book's paging-policy.py program

import re
import sys
import math

PAGESIZE = 4096  # find this value from 'getconf PAGESIZE' linux command
REGEX = "^.. (.+),\d" # corresponds to mem trace format of valgrind lackey

regex = re.compile(REGEX)
bits = int(math.log(PAGESIZE,2)) # bits = number of bits devoted to offset within page

for line in sys.stdin:
	m = regex.match(line)
	if (m):
		# convert hex to int
		astr = m.group(1)
		hexstr = "0x"+astr
		address = int(hexstr, 16)

		# extract the page number
		pageno = address >> bits
		
		print(pageno)
