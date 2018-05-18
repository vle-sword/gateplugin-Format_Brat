RANDOM MUSINGS
- most of the time we can load docs without the annotation.conf file
  - however, it is useful for knowing we have boolean attributes etc.
  - maybe a PR that adds missing stuff given an conf file to a doc

KNOWN BUGS
- annotations are indexed by ID but equivalence relations all share the * ID so
  only the last one will be retained
- JSON ouput doesn't include equivalence relations
