# HaikuGenerator
## Uses the Stanford Log-linear Part-Of-Speech Tagger

This program is an implementation of a genetic algorithm that generates random Haiku poems.
There is a database of words that are randomly chosen to fill a chromosome. The fitness functions
consider the parts of speech by using a POS Tagger library. Also there is an implementation of
a syllable checker to match the criteria for a Haiku. I use an overall goal chromosome template that
incorporates parts of speech to configure a Haiku that flows and makes sense.
