#! /bin/bash

convert "$@" -crop 1350x960+0+0 "$@"_l.png
convert "$@" -crop 250x960+1350+0 "$@"_r.png
