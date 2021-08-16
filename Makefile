# Make will use bash instead of sh
SHELL := /usr/bin/env bash

.PHONY: create
create:
	@source scripts/create.sh
