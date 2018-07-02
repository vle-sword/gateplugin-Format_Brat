# brat Document Format

Provides support for loading/saving annotations stored in the [brat
standoff](http://brat.nlplab.org/standoff.html) format

## Loading Documents

To load documents in the brat format make sure you have the `.ann` file next to
the `.txt` file and that, extension excepting, the files share the same name.
Now load the `.txt` file into GATE in any of the normal ways specifying
`text/x-brat` as the MIME type.


## Saving Documents

To save documets in the brat format, right click on the document or corpus, and
select the _brat Standoff Annotations_ option. This will save just the
annotations (i.e. it will produce the `.ann` file) with offsets relative to the
document as visible in GATE. This means that if the document wasn't created
from a plain text file the offsets will be dependent on how the relevant GATE
document format unpacked the original document.

## Normalization

brat annotations can include normalization information ([see the docs for more
details](http://brat.nlplab.org/normalization.html)) which link annotations
to entities in a remote knowledge base; the examples link annotations to
Wikipedia entries for example.

The normalization information present in the `.ann` files is preserved upon
document loading, but is left in it's raw encoded form. The *brat Noramlizer*
PR can be used to expand this information into full URLs using the details
found in the relevant `tools.conf` file.

The PR can also be configured to collapse the full URLs back to their encoded
form ready for saving as brat annotations again should this be necessary using
the `type` parameter.
