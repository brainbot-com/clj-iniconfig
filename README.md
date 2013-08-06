# iniconfig

A minimal Clojure library designed to read .ini files compatible with
python's py.iniconfig, i.e. it uses the number sign '#' as comment
character and allows line continuations.
## Artifacts

iniconfig artifacts are
[released to Clojars](https://clojars.org/com.brainbot/iniconfig). If
you are using Maven, add the following repository definition to your
`pom.xml`:

	<repository>
	  <id>clojars.org</id>
	  <url>http://clojars.org/repo</url>
	</repository>

### The Most Recent Release

With [Leiningen](http://leiningen.org):

	[com.brainbot/iniconfig "0.1.1"]

With Maven:

	<dependency>
	  <groupId>com.brainbot</groupId>
	  <artifactId>iniconfig</artifactId>
	  <version>0.1.1</version>
	</dependency>

## Usage

	user=> (require 'com.brainbot.iniconfig)
	nil
	user=> (com.brainbot.iniconfig/read-ini "example.ini")
	{"pytest" {"norecursedirs" "bin parts develop-eggs eggs .* _* CVS {args}"}, "testenv:py25" {"deps" "pytest>=2.3\n     WebTest==1.4.3\n     WebOb==0.9.6.1\n     BeautifulSoup==3.2.1"}, "testenv" {"sitepackages" "False", "commands" "py.test []", "deps" "pytest>=2.3\n     webtest\n     beautifulsoup4"}, "tox" {"envlist" "py25,py26,py27,py32,py33"}}


## License

Copyright © 2013 brainbot technologies AG

Distributed under the Eclipse Public License, the same as Clojure.
