# Make will use bash instead of sh
SHELL := /usr/bin/env bash

# all
.PHONY: all
all: terraform create
	@echo "Done"

.PHONY: create
create:
	@source scripts/create.sh

.PHONY: acceptance-tests
acceptance-tests: create
	@source scripts/run-acceptance-tests.sh

.PHONY: terraform
terraform:
	@source scripts/terraform.sh
