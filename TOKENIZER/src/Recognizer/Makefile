TEMPLATE := RecIdent.java RecString.java RecComment.java

# COMMAND

template: RecIdent.java RecString.java RecComment.java
	git add $(TEMPLATE)
	git commit -a -m "ADDED: $(TEMPLATE)"
	git push
	
# GENERIC RULES

Rec%.java: 
	@sed -e "s|TEMPLATE|$*|g ; s|template|$(shell echo $* | tr A-Z a-z)|g ; s|USER|$(USER)|g" RecTEMPLATE.java > $@
