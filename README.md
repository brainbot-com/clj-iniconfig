# iniconfig

A minimal Clojure library designed to read .ini files compatible with
python's py.iniconfig, i.e. it uses the number sign '#' as comment
character and allows line continuations.

## Installation

iniconfig artifacts are
[released to Clojars](https://clojars.org/com.brainbot/iniconfig).

If you are using [Leiningen](http://leiningen.org), add the following
to your `project.clj`'s dependencies:

``` clj
	[com.brainbot/iniconfig "0.2.0"]
```

## Usage

iniconfig has only one namespace `com.brainbot.iniconfig` with two
public functions:

``` clj
=> (require '[com.brainbot.iniconfig :as iniconfig])
```

`read-ini` will read an ini file:

``` clj
=> (spit "test.ini" "[main]\nmsg = foo\n  bar\n[email]\nfrom = ralf@systemexit.de")
=> (iniconfig/read-ini "test.ini")
{"email" {"from" "ralf@systemexit.de"}, "main" {"msg" "foo\n  bar"}}
```

It's also possible to load an ini file via HTTP:

``` clj
=> (iniconfig/read-ini "https://raw.github.com/brainbot-com/clj-iniconfig/master/example.ini")
{"pytest" {"norecursedirs" "bin parts develop-eggs eggs .* _* CVS {args}"}, "testenv:py25" {"deps" "pytest>=2.3\n     WebTest==1.4.3\n     WebOb==0.9.6.1\n     BeautifulSoup==3.2.1"}, "testenv" {"sitepackages" "False", "commands" "py.test []", "deps" "pytest>=2.3\n     webtest\n     beautifulsoup4"}, "tox" {"envlist" "py25,py26,py27,py32,py33"}}
```

`read-ini-string` will parse an ini file supplied as string:

``` clj
=> (iniconfig/read-ini-string "[main]\nmsg = foo\n  bar\n[email]\nfrom = ralf@systemexit.de")
{"email" {"from" "ralf@systemexit.de"}, "main" {"msg" "foo\n  bar"}}
```

Both functions set the metadata field `:source`, which might be useful
for error reporting:

``` clj
=> (meta (iniconfig/read-ini "test.ini"))
{:source "test.ini"}

=> (meta (iniconfig/read-ini-string "[main]\nmsg = foo\n  bar\n[email]\nfrom = ralf@systemexit.de"))
{:source string}
```

## License

Copyright © 2013 brainbot technologies AG

Distributed under the Eclipse Public License, the same as Clojure.
