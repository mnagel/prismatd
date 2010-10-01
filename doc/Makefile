all: clean html

clean:
	rm -r html

html: html/screenshots
	zim --export --format=html -o html ./notebook.zim

html/screenshots:
	[ -d html ] || mkdir html
	# In theory a link would be sufficient here.  However if you want to
	# install the html directory as a documentation tree somewhere else
	# later, you'd need to take care of copying the contents of the folder
	# the symlink points to.  Thus just copy it, the clean rule above will
	# take care of deleting it first.
	cp -r screenshots html

.PHONY: all clean html html/screenshots

