# operator-notifier

Creates a notification

## Inputs

* value (any): Any value triggers a notification. The value can be used in the notification.

## Outputs

* None

## Configs

* notifier-url (string): URL of the notifier service in the form "http[s]://url:port" (default: "http://api.notifier:5000")
* title (string): Title of the message. May be a format string with value as its only argument.
* message (string): Body of the message. May be a format string with value as its only argument.