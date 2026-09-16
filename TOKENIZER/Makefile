workspace := /home/anais/AG/TP_TOKENIZER
login_gricad := belala

# == DO NOT MODIFY WHAT FOLLOWS ==

# PREDEFINED VARIABLES

project := TOKENIZER
package := Recognizer
tool := Tokenizer

PROJECT := $(workspace)/$(project)
SOURCE := $(PROJECT)/src/$(package)
CP = $(PROJECT)/bin

main := $(package).$(tool)
EXEC := $(CP)/$(subst .,/,$(main)).class
JAVA_FILES := $(wildcard $(SOURCE)/*.java)

TESTDIR := TEST
TEST    := $(wildcard $(TESTDIR)/OK/*)
ERROR   := $(wildcard $(TESTDIR)/ERROR/*)

# COMMANDS

.PHONY: check jpg exec test demo template clean

check: $(CP)/
	@clear
	@echo "\033[90m pwd       =\033[0m"  $(shell pwd)
	@echo "\033[90m workspace =\033[0m"  $(shell cd $(workspace) ; pwd)
	@echo "\033[90m PROJECT   =\033[0m"  $(shell cd $(PROJECT) ; pwd)
	@echo "\033[90m CP        =\033[0m"  $(shell cd $(CP) ; pwd)
	@echo "\033[93m main      =\033[0m"  $(main)
	@echo "\033[93m SOURCE    =\033[0m"  $(shell cd $(SOURCE) ; pwd)
	@echo "\033[93m ls *.java =\033[0m"  $(shell cd $(SOURCE) ; ls *.java)
	@echo "\033[94m TESTDIR   =\033[0m"  $(shell cd $(TESTDIR) ; pwd)
	@echo "\033[94m     ls -F =\033[0m"  $(shell cd $(TESTDIR) ; ls -F) 
	@echo "\033[96m EXEC      =\033[0m"  $(EXEC)
	@echo "\033[95m which sed =\033[0m"  $(shell which sed)
	@echo "\033[95m which tar =\033[0m"  $(shell which tar)
	@echo "\033[95m which dot =\033[0m"  $(shell which dot)

jpg:
	$(MAKE) -silent -C DOT/ jpg

exec: $(EXEC)

demo: exec 
	java -cp $(CP)/ $(main) README.md

all: exec test

clean:
	$(MAKE) -silent -C DOT/ clean
	$(if $(value CP), rm -fr $(CP)/, $(error CP is not set))

test: $(TEST:%=%.tested)

error: $(ERROR:%=%.tested)

template:
	git branch $(login_gricad)
	git switch $(login_gricad)
	git push origin $(login_gricad)
	$(MAKE) -silent -C src/$(package)/ template
	$(MAKE) -silent -C DOT/ template

# RULES 

$(CP)/ :
	-mkdir $@

$(EXEC) : $(JAVA_FILES) $(CP)/
	javac -cp $(CP)/ -d $(CP)/ $(JAVA_FILES)

%.tested: % $(EXEC)
	java -cp $(CP)/ $(main) $<
