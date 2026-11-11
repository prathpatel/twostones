# Scripts to run with cron on the TwoStones server

Both are optional. You don't need them on a development machine.

- `geoip-update-db.sh` downloads MaxMind's free GeoLite2 City database into `data/`, which the
  security module uses to show where logins come from (`geoip` in `conf/base.conf`). It needs a
  free MaxMind license key in `MAXMIND_KEY`. Monthly is enough.
- `mongodb-report-score-decay.js` lowers the score of old moderation reports, so that recent
  ones come first. Run it once a day.

Example crontab, with the code in `/opt/twostones` and the database `twostones`:

```
# m h dom mon dow user   command
0  3 * * *  twostones  mongosh --quiet "$(grep -oE 'mongodb://[^"]+' /opt/twostones/conf/secrets.conf | head -1)" /opt/twostones/cron/mongodb-report-score-decay.js
0  4 1 * *  twostones  MAXMIND_KEY=your-key /opt/twostones/cron/geoip-update-db.sh
```
