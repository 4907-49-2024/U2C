# Module Purpose
Contains Filters, which are components which modify or transform data.

## Model Demultiplexer
### Element name hardcodes
| ElementType (IN) | DiagramType   | Primitive (OUT) |
|------------------|---------------|-----------------|
| message          | collaboration | stimuli         |
| lifeline         | collaboration | agent           |

### Parsing Assumptions
- Messages have exactly 1 ":" character, with the message sequence number before it