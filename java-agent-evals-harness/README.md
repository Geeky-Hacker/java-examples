# java-agent-evals-harness

A small rule-based Java agent exposed on `/run`, goldens in JSONL, a harness-evals config and a plain Java tool correctness scorer. The tests show an agent that passes its unit tests but scores 0.5 on a combined request.

Run the tests with `mvn verify`. To evaluate with harness-evals, start `AgentServer` and run `harness-evals run support-agent.eval.yaml`.

# Related articles

- [Evaluating Java AI agents with harness-evals beyond unit tests](https://www.geekyhacker.com/evaluating-java-ai-agents-harness-evals/)
